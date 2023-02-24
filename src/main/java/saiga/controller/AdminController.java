package saiga.controller;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import saiga.payload.request.AddressRequest;
import saiga.payload.request.TopUpBalanceRequest;
import saiga.service.AdminService;

import javax.validation.Valid;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 24 Feb 2023
 **/
@RestController
@RequestMapping("/api/admin")
public record AdminController(
        AdminService adminService
) {

    @PostMapping(value = "/static-address")
    public HttpEntity<?> createStaticAddress(@RequestBody @Valid AddressRequest addressRequest) {
        return adminService.createStaticAddress(addressRequest).handleResponse();
    }

    @GetMapping(value = "/cabinets")
    public HttpEntity<?> getAllCabinets() {
        return adminService.getAllCabinets().handleResponse();
    }

    @GetMapping(value = "/static-addresses")
    public HttpEntity<?> getAllStaticAddresses() {
        return adminService.getAllStaticAddresses().handleResponse();
    }

    @PostMapping(value = "top-up-balance")
    public HttpEntity<?> topUpBalance(@RequestBody @Valid TopUpBalanceRequest topUpBalanceRequest) {
        return adminService.topUpBalance(topUpBalanceRequest).handleResponse();
    }

}
