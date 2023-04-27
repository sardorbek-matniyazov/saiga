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
import saiga.utils.statics.GlobalMethodsToHelp;
import saiga.utils.statics.MessageResourceHelperFunction;

import java.math.BigDecimal;

import static saiga.model.enums.OrderStatus.ORDERED;
import static saiga.utils.statics.Constants._ORDER_TAX;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 26 Feb 2023
 **/
@Service
public record DriverServiceImpl(
        CabinetRepository cabinetRepository,
        OrderRepository orderRepository,
        MessageResourceHelperFunction messageResourceHelper,
        GlobalMethodsToHelp globalMethodsToHelp
) implements DriverService {
    @Override
    public MyResponse getBalanceInOut() {
        final User currentUser = globalMethodsToHelp.getCurrentUser();
        final Cabinet currentCabinet = cabinetRepository.findByUserId(currentUser.getId()).orElseThrow(
                () -> new NotFoundException(
                        String.format(
                                messageResourceHelper.apply("cabinet.not_found_with_user_id"),
                                currentUser.getId()
                        )
                )
        );

        final BigDecimal benefit         = orderRepository.sumOfBenefitByCabinetToIdAndStatus(currentCabinet.getId(), ORDERED);
        final Double balanceOut         = orderRepository.countByCabinetToIdAndStatus(currentCabinet.getId(), ORDERED);

        final BalanceInOutDTO balance = new BalanceInOutDTO(
                currentCabinet.getBalance(),
                benefit == null ? BigDecimal.ZERO : benefit,
                balanceOut == null ? BigDecimal.ZERO : _ORDER_TAX.multiply(BigDecimal.valueOf(balanceOut))
        );

        return MyResponse._OK()
                .setMessage("Balance in out")
                .addData("data", balance);
    }
}
