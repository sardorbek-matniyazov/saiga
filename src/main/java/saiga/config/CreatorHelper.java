package saiga.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import saiga.model.Cabinet;
import saiga.model.Role;
import saiga.model.User;
import saiga.model.enums.RoleEnum;
import saiga.repository.CabinetRepository;
import saiga.repository.RoleRepository;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 26 Mar 2023
 **/
@Component
public class CreatorHelper implements CommandLineRunner {

    private final CabinetRepository cabinetRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public CreatorHelper(CabinetRepository cabinetRepository, RoleRepository roleRepository) {
        this.cabinetRepository = cabinetRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        cabinetRepository.save(
                new Cabinet(
                        new User(
                                "Kyuto",
                                "Seco",
                                "phone",
                                roleRepository.findByRole(RoleEnum.ADMIN).orElse(
                                        new Role(
                                                RoleEnum.ADMIN
                                        )
                                ),
                                "Bearer 1234567890"
                        )
                )
        );
    }
}
