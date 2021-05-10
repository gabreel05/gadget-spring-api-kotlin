package br.com.mountech.gadget

import javax.persistence.*

@Entity
@Table(name = "GADGET")
data class Gadget(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val gadgetId: Long,
    val gadgetName: String,
    val gadgetCategory: String?,
    val gadgetAvailability: Boolean = true,
    val gadgetPrice: Double
)
