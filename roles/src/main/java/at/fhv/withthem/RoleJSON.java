package at.fhv.withthem;

import java.util.ArrayList;

public class RoleJSON {
        private int game;

        private ArrayList<Integer> roles;

        public RoleJSON(int game, ArrayList<Integer> roles) {
            this.game = game;
            this.roles = roles;
        }

        public int getGame() {
            return game;
        }

        public ArrayList<Integer> getRoles() {
            return roles;
        }
}
