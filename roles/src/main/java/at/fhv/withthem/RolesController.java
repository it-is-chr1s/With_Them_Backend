package at.fhv.withthem;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class RolesController {

    private Roles roles;

    public RolesController() {

        roles = new Roles(10);
    }

    @PostMapping("/roles/")
    public boolean setRoles(@RequestBody RoleJSON roles) {
        this.roles.setRoles(roles.getGame(),roles.getRoles());
        return true;
    }
    @PostMapping("/players/{game}/{players}")
    public boolean setPlayers(@PathVariable int game,@PathVariable int players) {

        roles.setPlayers(game,players);
        return true;
    }
    @GetMapping("/roles/{game}/{id}")
    public int getRole(@PathVariable int game,@PathVariable int id) {
       return roles.getRole(game,id);
    }


}
