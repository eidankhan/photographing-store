package com.photographing.store.app.modal;

import javax.persistence.*;

// This class creates table with name 'files' in the database
@Entity
@Table(name = "files")
public class File {
    // This column is the primary key of the files table
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Value for this column is increased by one when a new record is added
    private Long id;
    private String name;
    private String url;


    public File() {
    }

    public File(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
