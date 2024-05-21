package at.fhv.withthem.GameLogic.Maps;

import org.springframework.stereotype.Component;

public class PolusMap extends GameMap{

        public PolusMap() {
                super(47, 82);

                //first row & column are not working correctly, so we need this
                setWalls(0, 0, 82, 1);
                setWalls(0, 1, 1, 46);

                initializeMapLayout();
        }
        public void initializeMapLayout() {
                setWalls(1, 1, 3, 17); // 1
                setWalls(4, 1, 3, 15); // 2
                setWalls(6, 16, 1, 4); // 3
                setWalls(7, 1, 3, 19); // 4
                setWalls(10, 1, 8, 7); // 5
                setWalls(10, 10, 6, 5); // 6
                setWalls(10, 17, 3, 6); // 7
                setWalls(18, 1, 16, 10); // 8
                setWalls(18, 13, 9, 2); // 9
                setWalls(18, 15, 7, 4); // 10
                setWalls(15, 17, 3, 6); // 11
                setWalls(23, 19, 2, 6); // 12
                setWalls(23, 25, 12, 3); // 13
                setWalls(1, 30, 3, 17); // 14
                setWalls(4, 32, 2, 15); // 15
                setWalls(6, 28, 3, 19); // 16
                setWalls(9, 28, 4, 3); // 17
                setWalls(10, 25, 3, 3); // 18
                setWalls(15, 25, 3, 6); // 19
                setWalls(18, 28, 9, 8); // 20
                setWalls(9, 33, 7, 6); // 21
                setWalls(9, 41, 12, 6); // 22
                setWalls(18, 38, 3, 3); // 23
                setWalls(24, 36, 3, 6); // 24
                setWalls(21, 44, 16, 3); // 25
                setWalls(34, 1, 1, 3); // 26
                setWalls(35, 1, 1, 2); // 27
                setWall(36, 1); // 28
                setWalls(30, 13, 4, 2); // 29
                setWalls(32, 15, 2, 6); // 30
                setWalls(34, 18, 1, 3); // 31
                setWalls(35, 19, 1, 3); // 32
                setWalls(36, 20, 1, 2); // 33
                setWall(37, 21); // 34
                setWalls(35, 22, 8, 6); // 35
                setWalls(36, 28, 7, 3); // 36
                setWalls(34, 31, 3, 11); // 37
                setWalls(30, 37, 4, 5); // 38
                setWalls(40, 34, 5, 7); // 39
                setWalls(48, 38, 7, 4); // 40
                setWalls(48, 42, 1, 4); // 41
                setWalls(48, 46, 10, 1); // 42
                setWalls(46, 22, 11, 4); // 43
                setWalls(46, 28, 3, 3); // 44
                setWalls(48, 31, 1, 2); // 45
                setWalls(48, 33, 14, 3); // 46
                setWalls(62, 33, 1, 2); // 47
                setWalls(57, 38, 5, 1); // 48
                setWalls(57, 39, 6, 2); // 49
                setWalls(57, 41, 3, 1); // 50
                setWalls(58, 42, 8, 5); // 51
                setWall(52, 1); // 52
                setWalls(53, 1, 1, 2); // 53
                setWalls(54, 1, 1, 3); // 54
                setWalls(55, 13, 8, 4); // 55
                setWalls(61, 17, 2, 2); // 56
                setWalls(55, 17, 2, 1); // 57
                setWalls(54, 18, 3, 1); // 58
                setWalls(53, 19, 4, 1); // 59
                setWalls(52, 20, 5, 1); // 60
                setWalls(51, 21, 6, 1); // 61
                setWalls(52, 29, 4, 2); // 62
                setWalls(57, 23, 6, 3); // 63
                setWalls(61, 21, 2, 2); // 64
                setWalls(63, 21, 5, 4); // 65
                setWalls(60, 26, 3, 7); // 66
                setWalls(55, 1, 7, 9); // 67
                setWalls(62, 1, 20, 5); // 68
                setWalls(68, 6, 14, 13); // 69
                setWalls(66, 13, 2, 6); // 70
                setWalls(71, 19, 6, 2); // 71
                setWalls(71, 23, 6, 2); // 72
                setWalls(71, 25, 11, 22); // 73
                setWalls(66, 27, 5, 8); // 74
                setWalls(67, 35, 4, 4); // 75
                setWalls(66, 39, 5, 8); // 76

                setMeetingPoint(44, 11);

                setTask(56, 30, "Connecting Wires", 1); // Admin
                setTask(36, 2, "Connecting Wires", 2); // Cafeteria
                setTask(53, 45, "Connecting Wires", 3); // Communications
                setTask(31, 28, "Connecting Wires", 4); // Electrical
                setTask(11, 15, "Connecting Wires", 5); // Align Engine
                setTask(10, 39, "Connecting Wires", 6); // Align Engine
                setTask(34, 23, "Connecting Wires", 7); // MedBay
                setTask(74, 21, "Connecting Wires", 8); // Navigation
                setTask(78, 19, "File Download", 9); // Navigation
                setTask(80, 19, "File Upload", 9); // Navigation
                setTask(81, 21, "Connecting Wires", 10); // Navigation
                setTask(57, 19, "Connecting Wires", 11); // o2
                setTask(1, 24, "Connecting Wires", 12); // o2
                // Security, Shields, Storage, Weapons are not implemented
        }
}

