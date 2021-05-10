package br.com.mountech.gadget

import org.springframework.data.jpa.repository.JpaRepository

interface GadgetRepository : JpaRepository<Gadget, Long>
