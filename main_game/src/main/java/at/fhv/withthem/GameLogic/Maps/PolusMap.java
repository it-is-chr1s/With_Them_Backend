package at.fhv.withthem.GameLogic.Maps;

import org.springframework.stereotype.Component;

public class PolusMap extends GameMap{

        public PolusMap() {
                super(46, 81);

                initializeMapLayout();
        }
        public void initializeMapLayout() {
                setWalls(0, 0, 3, 17); // 1
                setWalls(3, 0, 3, 15); // 2
                setWalls(5, 15, 1, 4); // 3
                setWalls(6, 0, 3, 19); // 4
                setWalls(9, 0, 8, 7); // 5
                setWalls(9, 9, 6, 5); // 6
                setWalls(9, 16, 3, 6); // 7
                setWalls(17, 0, 16, 10); // 8
                setWalls(17, 12, 9, 2); // 9
                setWalls(17, 14, 7, 4); // 10
                setWalls(14, 16, 3, 6); // 11
                setWalls(22, 18, 2, 6); // 12
                setWalls(22, 24, 12,3); // 13
                setWalls(0, 29, 3, 17); // 14
                setWalls(3, 31, 2, 15); // 15
                setWalls(5, 27, 3, 19); // 16
                setWalls(8, 27, 4, 3); // 17
                setWalls(9, 24, 3, 3); // 18
                setWalls(14, 24, 3, 6); // 19
                setWalls(17, 27, 9, 8); // 20
                setWalls(8, 32, 7, 6); // 21
                setWalls(8, 40, 12, 6); // 22
                setWalls(17, 37, 3, 3); // 23
                setWalls(23, 35, 3, 6); // 24
                setWalls(20, 43, 16, 3); // 25
                setWalls(33, 0, 1, 3); // 26
                setWalls(34, 0, 1, 2); // 27
                setWall(35, 0); // 28
                setWalls(29, 12, 4, 2); // 29
                setWalls(31, 14, 2, 6); // 30
                setWalls(33, 17, 1, 3); // 31
                setWalls(34, 18, 1, 3); // 32
                setWalls(35, 19, 1, 2); // 33
                setWall(36, 20); // 34
                setWalls(34, 21, 8, 6); // 35
                setWalls(35, 27, 7, 3); //36
                setWalls(33, 30, 3, 11); // 37
                setWalls(29, 36, 4, 5); // 38
                setWalls(39, 33, 5, 7); // 39
                setWalls(47, 37, 7, 4); // 40
                setWalls(47, 41, 1, 4); // 41
                setWalls(47, 45, 10, 1); // 42
                setWalls(45, 21, 11, 4); // 43
                setWalls(45, 27, 3, 3); // 44
                setWalls(47, 30, 1, 2); // 45
                setWalls(47, 32, 14, 3); // 46
                setWalls(61, 32, 1, 2); // 47
                setWalls(56, 37, 5, 1); // 48
                setWalls(56, 38, 6, 2); //49
                setWalls(56, 40, 3, 1); // 50
                setWalls(57, 41, 8, 5); // 51
                setWall(51, 0); // 52
                setWalls(52, 0, 1, 2); // 53
                setWalls(53, 0, 1, 3); // 54
                setWalls(54, 12, 8, 4); // 55
                setWalls(60, 16, 2, 2); // 56
                setWalls(54, 16, 2, 1); // 57
                setWalls(53, 17, 3, 1); // 58
                setWalls(52, 18, 4, 1); // 59
                setWalls(51, 19, 5, 1); // 60
                setWalls(50, 20, 6, 1); // 61
                setWalls(51, 28, 4, 2); // 62
                setWalls(56, 22, 6, 3); // 63
                setWalls(60, 20, 2, 2); // 64
                setWalls(62, 20, 5, 4); // 65
                setWalls(59, 25, 3, 7); // 66
                setWalls(54, 0, 7, 9); // 67
                setWalls(61, 0, 20, 5); // 68
                setWalls(67, 5, 14, 13); // 69
                setWalls(65, 12, 2, 6); // 70
                setWalls(70, 18, 6, 2); // 71
                setWalls(70, 22, 6, 2); // 72
                setWalls(70, 24, 11, 22); // 73
                setWalls(65, 26, 5, 8); // 74
                setWalls(66, 34, 4, 4); // 75
                setWalls(65, 38, 5, 8); // 76

                setMeetingPoint(43, 10);

                setTask(55, 29, "Connecting Wires", 1); // Admin
                setTask(35, 1, "Connecting Wires", 2); // Cafeteria
                setTask(52, 44, "Connecting Wires", 3); // Communications
                setTask(30, 27, "Connecting Wires", 4); // Electrical
                setTask(10, 14, "Connecting Wires", 5); // Align Engine
                setTask(9, 38, "Connecting Wires", 6); // Align Engine
                setTask(33, 22, "Connecting Wires", 7); // MedBay
                setTask(73, 20, "Connecting Wires", 8); // Navigation
                setTask(77, 18, "File Download", 9); // Navigation
                setTask(79, 18, "File Upload", 9); // Navigation
                setTask(80, 20, "Connecting Wires", 10); // Navigation
                setTask(56, 18, "Connecting Wires", 11); // o2
                setTask(0, 23, "Connecting Wires", 12); // o2
                // Security, Shields, Storage, Weapons are not implemented
        }
}

