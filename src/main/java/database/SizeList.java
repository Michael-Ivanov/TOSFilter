package database;

import javax.persistence.*;

@Entity
@Table(name= "sizes")
public class SizeList {

    @Id
    @Column(name= "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name= "size")
    private int size;



}
