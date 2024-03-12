package smw.capstone.entity;

import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Board_Id;

    @JoinColumn(name = "Member_Id", nullable = false)
    @ManyToOne
    private Member member;

    private String title;
    private int likes;
    private LocalDate createAt;

    private LocalDate updateAt;

}
