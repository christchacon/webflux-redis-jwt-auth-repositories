package com.tutorial.tutorialwebflux.services;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;

import com.tutorial.tutorialwebflux.daos.BeneficiariesDao;
import com.tutorial.tutorialwebflux.handlers.BeneficiariesHandlers;
import com.tutorial.tutorialwebflux.models.BeneficiaryEntity;
import com.tutorial.tutorialwebflux.models.BussinessFormatConfigEntity;
import com.tutorial.tutorialwebflux.repositories.BeneficiariesRepository;
import com.tutorial.tutorialwebflux.repositories.BusinessFormatRepository;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Supplier;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.file.Paths;
import java.time.Duration;

import org.springframework.util.FileSystemUtils;

@Service
@Component
//@CacheConfig(cacheNames = "beneficiaries")
public class BeneficiariesService {
    
    private Logger logger = LoggerFactory.getLogger(BeneficiariesService.class);
    @Autowired
    BeneficiariesRepository bRepository;
    @Autowired
    BusinessFormatRepository businessFormatRepository;
    @Autowired
    ReactiveRedisTemplate<String, Object> redisTemplate;
    @Autowired
    BeneficiariesDao beneficiariesDao;

    @Value("${beneficiary.time-to-live}")
    Long bttl;

    private String regexendspaces = "\\s*$";


    @Transactional
    //@Cacheable(value="beneficiaries")
    public Mono<BeneficiaryEntity> getById(Long id){
        //logger.info("Search beneficiary {} into Data Base", id);
        String key = "beneficiary::" + id;
       
        return  redisTemplate.opsForValue().get(key)
            //.doOnSubscribe(s -> logger.info("Try read from Redis: {}", key))
            .cast( BeneficiaryEntity.class)
                .doOnNext(beneficiary ->
                    logger.info("Beneficiary: {} found into cache",beneficiary.getId())
                    )
                .onErrorResume(error -> {
                    logger.info("An error: {} ocurred getting Beneficiary into cache", error.getMessage());
                    //Retornar empty para que se active el switchIfEmpty
                    return Mono.empty();
                })
            .switchIfEmpty(
                //Esto se ejecuta solo si realmente hay un Mono vacio que es cuando falla la obtencion Redis o es vacio
                Mono.defer(() -> { 
                    return bRepository.findById(id)
                        //beneficiariesDao.getBeneficiaryById(id)
                            .doOnNext(beneficiary ->
                                                logger.info("Beneficiary: {} found into data base",beneficiary.getId()))
                            .flatMap(beneficiary -> 
                                redisTemplate.opsForValue().set(key, beneficiary, Duration.ofMinutes(bttl))
                                // .doOnNext(value ->
                                //     logger.info("Beneficiary put into cache {}",value))
                                .onErrorResume(error -> {
                                    logger.info("An error: {} ocurred put Beneficiary into cache", error.getMessage());
                                    return Mono.empty();
                                })
                                .then(Mono.just(beneficiary))
                                );
                }).cast(BeneficiaryEntity.class)
            );
    }

    @Transactional
    // public Flux<BeneficiaryEntity> saveBeneficiaries(List<BeneficiaryEntity> beneficiaries){
    //     return bRepository.saveAll(beneficiaries).doOnNext(b -> {
    //          logger.info("✔ Insertado: " + b.getRut() + " con ID: " + b.getId());
    //     })
    //     .onErrorContinue((throwable, obj) -> {
    //         BeneficiaryEntity failed = (BeneficiaryEntity) obj;
    //          logger.error("❌ Error al insertar: " + failed.getRut() + " → " + throwable.getMessage());
    //     });
    //     //return beneficiariesDao.saveAll(beneficiaries);
    // }

        /**
     * Inserta una lista de BeneficiaryEntity usando ReactiveCrudRepository
     * - Muestra logs por cada entidad insertada correctamente (con su ID generado)
     * - Maneja errores por fila sin detener el flujo completo
     */
    public Flux<BeneficiaryEntity> saveBeneficiaries(List<BeneficiaryEntity> beneficiaries) {
        return bRepository.saveAll(beneficiaries) // ⬅️ Inserta todas las entidades reactivamente
            // .doOnNext(b -> {
            //     // ✅ Se ejecuta cuando una entidad fue insertada correctamente
            //     // Aquí ya tiene su ID asignado por la base de datos
            //     System.out.println("✔ Insertado: " + b.getRut() + " con ID: " + b.getId());
            // })
            .onErrorContinue((throwable, obj) -> {
                // ⚠️ Si ocurre un error al insertar una fila, NO detiene el flujo
                // Imprime el RUT de la entidad fallida y el mensaje del error
                BeneficiaryEntity failed = (BeneficiaryEntity) obj;
               logger.error("❌ Error al insertar: " + failed.getRut() + " → " + throwable.getMessage());
            });
    }

    // @Transactional
    // public Mono<Integer> saveBeneficiariesBatch(List<BeneficiaryEntity> beneficiaries){
    //    return beneficiariesDao.batchInsert(beneficiaries);
    // }

    @Transactional
    public Flux<BussinessFormatConfigEntity> getBFCE(Long id){
        return businessFormatRepository.getAllByIdConfigFormat(id);
    }

    public Flux<List<String>> readFile(ServerRequest request){
        
        //request.multiparData posee la referencia al archivo enviado en la petición
        return request.multipartData()
        .map(filePart -> filePart.get("file"))
        .flatMapMany(Flux::fromIterable)
        //.cast(FilePart.class)
        .flatMap(filePart ->
            //filePart.transferTo(null)
            filePart
            .content()
                .map(dataBuffer -> {
                    logger.info("Begin red File {}", filePart.name());
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    String result = new String(bytes, StandardCharsets.UTF_8);
                    //logger.info(result);
                    Supplier<Stream<String>> streamSupplier = result::lines;
                    //var isFileOk = streamSupplier.get().anyMatch(null);
                    logger.info("End read File {} read succesful", filePart.name());
                    return streamSupplier.get().collect(Collectors.toList());
                })
        );
    }

    public Flux<Object> upLoadFile(ServerRequest request){
        
        return request.multipartData()
        .map(filePart -> filePart.get("file"))
        .flatMapMany(Flux::fromIterable)
        .cast(FilePart.class)
        .flatMap(filePart ->{
            //filePart.transferTo(null)
            filePart.transferTo(Paths.get("/tmp/" + filePart.filename()));
            return Mono.just("ok");
        }
        );
    }

    public List<BeneficiaryEntity> getBeneficiary(List<String> linesFile, Mono<List<BussinessFormatConfigEntity>> bfe){
        
        List<BeneficiaryEntity> lb = new ArrayList<>();
        List<BussinessFormatConfigEntity> lbfe = bfe.block();
        logger.info("Begin extract {} beneficiaries", linesFile.size());
        linesFile
            .forEach(line -> {
                BeneficiaryEntity beneficiary = new BeneficiaryEntity();
                lbfe
                    .forEach(bfc ->{
                            switch (bfc.getDescAttribute()) {
                                case "rut":
                                    beneficiary.setRut(line.substring(bfc.getBeginChapter(), bfc.getBeginChapter()
                                                +bfc.getLongChapters()).replaceAll(regexendspaces, ""));
                                    break;
                                case "names":
                                    beneficiary.setNames(line.substring(bfc.getBeginChapter(), bfc.getBeginChapter()
                                                +bfc.getLongChapters()).replaceAll(regexendspaces, ""));
                                    break;

                                case "last_names":
                                    beneficiary.setLastNames(line.substring(bfc.getBeginChapter(), bfc.getBeginChapter()
                                                +bfc.getLongChapters()).replaceAll(regexendspaces, ""));
                                    break;

                                case "age":
                                    int age = Integer.parseInt(line.substring(bfc.getBeginChapter(), bfc.getBeginChapter()
                                                +bfc.getLongChapters()).replaceAll(regexendspaces, ""));
                                    beneficiary.setAge(age);
                                    break;
                                
                                case "bank":
                                    beneficiary.setBank(line.substring(bfc.getBeginChapter(), bfc.getBeginChapter()
                                                +bfc.getLongChapters()).replaceAll(regexendspaces, ""));
                                    break;
                                
                                case "account_type":
                                    beneficiary.setAccountType(line.substring(bfc.getBeginChapter(), bfc.getBeginChapter()
                                                +bfc.getLongChapters()).replaceAll(regexendspaces, ""));
                                    break;

                                case "account_number":
                                    beneficiary.setAccountNumber(line.substring(bfc.getBeginChapter(), bfc.getBeginChapter()
                                                +bfc.getLongChapters()).replaceAll(regexendspaces, ""));
                                    break;

                                default:
                                    // double amount = Double.parseDouble(line.substring(bfc.getBeginChapter(), bfc.getBeginChapter()
                                    //             +bfc.getLongChapters()).replaceAll(regexendspaces, ""));
                                    beneficiary.setAmount(new BigDecimal(line.substring(bfc.getBeginChapter(), bfc.getBeginChapter()
                                    +bfc.getLongChapters()).replaceAll(regexendspaces, "")));
                                    //Para formatear un double en string
                                    // String amount = String.format("$%,.2f", Double.valueOf(line.substring(bfc.getBeginChapter(), bfc.getBeginChapter()
                                    //              +bfc.getLongChapters()).replaceAll(regexendspaces, "")));
                                    // beneficiary.setAmount(amount);
                                    break;
                                }
                            }
                    );
                    lb.add(beneficiary);
                }
            );
        logger.info("End extract {} beneficiaries", linesFile.size());
        return lb;
    }
     
}
