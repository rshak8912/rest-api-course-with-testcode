package com.example.restfulwebservice.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
public class Post {
    @Id @GeneratedValue
    private Integer id;

    private String description;

    @ManyToOne(fetch= LAZY)
    @JsonIgnore
    private User user;

}
