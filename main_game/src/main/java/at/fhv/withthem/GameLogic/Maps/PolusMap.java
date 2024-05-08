package at.fhv.withthem.GameLogic.Maps;

import org.springframework.stereotype.Component;

public class PolusMap extends GameMap{

        public PolusMap() {
                super(50, 100);

                initializeMapLayout();
        }
        public void initializeMapLayout() {
                /*
                for (int i = 0; i < 50; i++) {
                        setWall(0,i);
                        setWall(100-1,i);
                }
                for (int i = 1; i < 100-1; i++) {
                        setWall(i,0);
                        setWall(i,50-1);
                }

                for(int i = 2; i < 20; i++){
                        setWall(39,i);
                        setWall(49,i);
                }

                for (int i = 2; i < 38; i++) {
                        setWall(i,20);
                        if(i<36){
                                setWall(i,24);
                        }

                }
                for (int i = 50; i < 100; i++) {
                        setWall(i,20);
                }
                for (int i = 50; i < 100; i++) {
                        setWall(i,20);

                } */

                setWall(5, 6);
                setWall(5, 5);

                setWall(7, 5);
                setWall(8, 5);
                setWall(9, 5);
                setWall(9, 6);
                setWall(9, 7);
                setWall(9, 8);
                setWall(9, 9);
                setWall(8, 9);
                setWall(7, 9);
                setWall(6, 9);
                setWall(5, 9);
                setWall(5, 8);

                setMeetingPoint(15, 3);
                //type should not include String %id
                setTask(7, 7, "Connecting Wires", 1);
                setTask(11, 5, "File Download", 2);
                setTask(11, 9, "File Upload", 2);

                setTask(15, 7, "Connecting Wires", 3);
                setTask(19, 5, "File Download", 4);
                setTask(19, 9, "File Upload", 4);
        }
}

