package com.venikkin.example.golftmts.model

import java.time.LocalDate
import java.util.Date
import javax.persistence.*

@Entity
@Table(name = "tournament")
class Tournament(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "name")
    val name: String,

    @Column(name = "courseName")
    val courseName: String,

    @Column(name = "countryCode")
    val countryCode: String,

    @Column(name = "startDate")
    val startDate: LocalDate,

    @Column(name = "endDate")
    val endDate: LocalDate,

    @Column(name = "creationTimestamp")
    var creationTimestamp: Date? = null,

    @Column(name = "rounds")
    val rounds: Int,

    @Column(name = "playerCount")
    val playerCount: Int? = null,

    @Column(name = "forecast")
    val forecast: String? = null,

    @Column(name = "externalId")
    val externalId: String,

    @Column(name = "provider")
    val provider: String
) {

    @PrePersist
    fun onPersist() {
        if (creationTimestamp == null) {
            creationTimestamp = Date();
        }
    }

}