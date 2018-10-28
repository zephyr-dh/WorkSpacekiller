package io.oacy.education.newbie.springbootnewbie.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "department")
public class Department implements Serializable {

    private Integer id;

    private String name;

    private String descr;
}
