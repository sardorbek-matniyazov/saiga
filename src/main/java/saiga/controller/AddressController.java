package saiga.controller;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saiga.model.enums.AddressType;
import saiga.payload.MyResponse;
import saiga.service.AddressService;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 02 Mar 2023
 **/
@RestController
@RequestMapping("/api/address")
public record AddressController (
        AddressService addressService
) {
    @GetMapping(value = "static-address")
    public HttpEntity<?> getStaticAddress() {
        return MyResponse._OK().addData("data", addressService.getAddressByType(AddressType.STATIC)).handleResponse();
    }
}
