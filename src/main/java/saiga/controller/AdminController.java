package saiga.controller;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saiga.payload.request.AddressRequest;
import saiga.payload.request.TopUpBalanceRequest;
import saiga.service.AdminService;

import javax.validation.Valid;

import static saiga.payload.MyResponse._OK;

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

    @GetMapping(value = "/static-address")
    public HttpEntity<?> getAllStaticAddress(@RequestBody @Valid AddressRequest addressRequest) {
        return _OK().addData("data", adminService.getAllStaticAddresses()).handleResponse();
    }

    @GetMapping(value = "cabinets")
    public HttpEntity<?> getAllCabinets() {
        return _OK().addData("data", adminService.getAllCabinets()).handleResponse();
    }

    @PostMapping(value = "top-up-balance")
    public HttpEntity<?> topUpBalance(@RequestBody @Valid TopUpBalanceRequest topUpBalanceRequest) {
        return adminService.topUpBalance(topUpBalanceRequest).handleResponse();
    }
}
