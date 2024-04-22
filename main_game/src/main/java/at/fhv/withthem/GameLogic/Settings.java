package at.fhv.withthem.GameLogic;

import java.util.HashMap;

public class Settings {
        private double _speed;
        private int _maxPlayers;
        private int _minPlayers;
        private int _timeLimit;

        private double _killRange;

        private HashMap<Integer, Integer> _roles = new HashMap<>();

        public Settings(){
            _speed=1.0;
            _maxPlayers=4;
            _minPlayers=2;
            _timeLimit=20;
            _killRange=0.5;
            _roles.put(1, 1);
        }


        public double getSpeed() {
                return _speed;
        }

        public void setSpeed(double speed) {
                _speed = speed;
        }

        public int getMaxPlayers() {
                return _maxPlayers;
        }

        public void setMaxPlayers(int maxPlayers) {
                _maxPlayers = maxPlayers;
        }

        public int getMinPlayers() {
                return _minPlayers;
        }

        public void setMinPlayers(int minPlayers) {
                _minPlayers = minPlayers;
        }

        public int getTimeLimit() {
                return _timeLimit;
        }

        public void setTimeLimit(int timeLimit) {
                _timeLimit = timeLimit;
        }

        public double getKillRange() {
                return _killRange;
        }

        public void setKillRange(double killRange) {
                _killRange = killRange;
        }

        public HashMap<Integer, Integer> getRoles() {
                return _roles;
        }

        public void setRoles(HashMap<Integer, Integer> roles) {
                _roles = roles;
        }
}
