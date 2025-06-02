package com.tutorial.tutorialwebflux.models;

import java.io.Serializable;
import org.springframework.stereotype.Component;

@Component
@org.springframework.data.relational.core.mapping.Table(name = "tbl_bussiness_format_config", schema = "test")
public class BussinessFormatConfigEntity implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @org.springframework.data.annotation.Id
    @org.springframework.data.relational.core.mapping.Column(value="id")
    private Long id;

    @org.springframework.data.relational.core.mapping.Column(value="id_config_format")
    private Long idBFC;
    
    @org.springframework.data.relational.core.mapping.Column(value="desc_attribute")
    private String descAttribute;

    @org.springframework.data.relational.core.mapping.Column(value="begin_chapter")
    private int beginChapter;
    
    @org.springframework.data.relational.core.mapping.Column(value="long_chapters")
    private int longChapters;
    
    public BussinessFormatConfigEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescAttribute() {
        return descAttribute;
    }

    public void setDescAttribute(String descAttribute) {
        this.descAttribute = descAttribute;
    }

    public int getBeginChapter() {
        return beginChapter;
    }

    public void setBeginChapter(int beginChapter) {
        this.beginChapter = beginChapter;
    }

    public int getLongChapters() {
        return longChapters;
    }

    public void setLongChapters(int longChapters) {
        this.longChapters = longChapters;
    }

    public Long getIdBFC() {
        return idBFC;
    }

    public void setIdBFC(Long idBFC) {
        this.idBFC = idBFC;
    }

    @Override
    public String toString() {
        return "BussinessFormatConfigEntity [id=" + id + ", idBFC=" + idBFC + ", descAttribute=" + descAttribute
                + ", beginChapter=" + beginChapter + ", longChapters=" + longChapters + "]";
    }
    
}
