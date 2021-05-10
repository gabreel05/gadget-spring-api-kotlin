package br.com.mountech.gadget

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.ObjectUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping(value = ["/api"])
class GadgetController(
    private val gadgetRepository: GadgetRepository
) {

    @GetMapping(value = ["/gadgets"])
    fun fetchGadgets(): ResponseEntity<List<Gadget>> {
        val gadgets = gadgetRepository.findAll()

        if (gadgets.isEmpty()) {
            return ResponseEntity<List<Gadget>>(HttpStatus.NO_CONTENT)
        }

        return ResponseEntity<List<Gadget>>(gadgets, HttpStatus.OK)
    }

    @GetMapping(value = ["/gadgets/{id}"])
    fun fetchGadgetById(
        @PathVariable(value = "id") gadgetId: Long
    ): ResponseEntity<Gadget> {
        val gadget = gadgetRepository.findById(gadgetId)

        if (gadget.isPresent) {
            return ResponseEntity<Gadget>(gadget.get(), HttpStatus.OK)
        }

        return ResponseEntity<Gadget>(HttpStatus.NOT_FOUND)
    }

    @PostMapping(value = ["/gadgets"])
    fun addNewGadget(
        @RequestBody gadget: Gadget,
        uri: UriComponentsBuilder
    ): ResponseEntity<Gadget> {
        val persistedGadget = gadgetRepository.save(gadget)

        if (ObjectUtils.isEmpty(persistedGadget)) {
            return ResponseEntity<Gadget>(HttpStatus.BAD_REQUEST)
        }

        val headers = HttpHeaders()

        headers.location =
            uri.path("/gadget/{gadgetId}").buildAndExpand(gadget.gadgetId).toUri()

        return ResponseEntity(headers, HttpStatus.CREATED)
    }

    @PutMapping(value = ["/gadgets/{id}"])
    fun updateGadgetById(
        @PathVariable(value = "id") gadgetId: Long,
        @RequestBody gadget: Gadget
    ): ResponseEntity<Gadget> {
        return gadgetRepository.findById(gadgetId).map { gadgetDetails ->
            val updatedGadget: Gadget = gadgetDetails.copy(
                gadgetCategory = gadget.gadgetCategory,
                gadgetName = gadget.gadgetName,
                gadgetPrice = gadget.gadgetPrice,
                gadgetAvailability = gadget.gadgetAvailability
            )

            ResponseEntity(gadgetRepository.save(updatedGadget), HttpStatus.OK)
        }.orElse(ResponseEntity<Gadget>(HttpStatus.INTERNAL_SERVER_ERROR))
    }

    @DeleteMapping(value = ["/gadgets/{id}"])
    fun removeGadgetById(
        @PathVariable(value = "id") gadgetId: Long
    ): ResponseEntity<Unit> {
        val gadget = gadgetRepository.findById(gadgetId)

        if (gadget.isPresent) {
            gadgetRepository.deleteById(gadgetId)

            return ResponseEntity<Unit>(HttpStatus.NO_CONTENT)
        }

        return ResponseEntity<Unit>(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @DeleteMapping(value = ["/gadgets"])
    fun deleteGadgets(): ResponseEntity<Unit> {
        gadgetRepository.deleteAll()

        return ResponseEntity<Unit>(HttpStatus.OK)
    }
}
