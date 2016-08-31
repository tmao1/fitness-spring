package com.chyld.entities;


import com.chyld.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;


@Entity
@Table(name = "profile")
@Data
public class Profile {
    private int id;
    private int version;
    private Gender gender;
    private int age;
    private float height;
    private float weight;
    private String photo;
    private User user;

    @Id
    @GeneratedValue
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    @Version
    public int getVersion() {
        return version;
    }
    public void setVersion(int version) {
        this.version = version;
    }

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('M', 'F')")
    public Gender getGender() {
        return gender;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Column(name="age")
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    @Column(name="height")
    public float getHeight() {
        return height;
    }
    public void setHeight(float height) {
        this.height = height;
    }
    @Column(name="weight")
    public float getWeight() {
        return weight;
    }
    public void setWeight(float weight) {
        this.weight = weight;
    }
    @Column(name="photo")
    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @OneToOne
    @JoinColumn(name="user_id")
    @JsonIgnore
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

}
