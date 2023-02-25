package saiga.service.impl;

import org.springframework.stereotype.Service;
import saiga.model.Cabinet;
import saiga.model.User;
import saiga.payload.MyResponse;
import saiga.payload.dto.BalanceInOutDTO;
import saiga.repository.CabinetRepository;
import saiga.repository.OrderRepository;
import saiga.service.DriverService;
import saiga.utils.exceptions.NotFoundException;

import java.math.BigDecimal;

import static saiga.model.enums.OrderStatus.ORDERED;
import static saiga.utils.statics.Constants._ORDER_TAX;
import static saiga.utils.statics.GlobalMethodsToHelp.getCurrentUser;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 26 Feb 2023
 **/
@Service
public record DriverServiceImpl(
        CabinetRepository cabinetRepository,
        OrderRepository orderRepository
) implements DriverService {
    @Override
    public MyResponse getBalanceInOut() {
        final User currentUser = getCurrentUser();
        final Cabinet currentCabinet = cabinetRepository.findByUserId(currentUser.getId()).orElseThrow(
                () -> new NotFoundException("Cabinet not found")
        );

        final BigDecimal benefit = orderRepository.sumOfBenefitByCabinetToIdAndStatus(currentCabinet.getId(), ORDERED);
        final Double balanceOut = orderRepository.countByCabinetToIdAndStatus(currentCabinet.getId(), ORDERED);

        final BalanceInOutDTO balance = new BalanceInOutDTO(
                currentCabinet.getBalance(),
                benefit == null  ? BigDecimal.ZERO : benefit,
                balanceOut == null ? BigDecimal.ZERO : _ORDER_TAX.multiply(BigDecimal.valueOf(balanceOut))
        );

        return MyResponse._OK()
                .setMessage("Balance in out")
                .addData("data", balance);
    }
}
