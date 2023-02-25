package saiga.controller;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saiga.service.DriverService;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 26 Feb 2023
 **/
@RestController
@RequestMapping("/api/driver")
public record DriverController(
        DriverService driverService
) {
    @GetMapping(value = "balance-in-out")
    public HttpEntity<?> getBalanceInOut() {
        return driverService.getBalanceInOut().handleResponse();
    }
}
