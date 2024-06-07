package at.fhv.withthem.tasks;

import org.springframework.stereotype.Component;

@Component
public class SabotageHandler {

    final int sabotageDuration_sec = 10;
    final int sabotageCooldown_sec = 30;

    int timerDuration_sec = sabotageDuration_sec;
    int timerCooldown_sec = sabotageCooldown_sec;

    public SabotageHandler(){
        if(sabotageDuration_sec > sabotageCooldown_sec){
            throw new IllegalArgumentException("Sabotage duration must be less than sabotage cooldown.");
        }
    }

    public void startSabotage(ReactionTimerChange durationChange, ReactionTimerChange cooldownChange) {
        if(timerDuration_sec == sabotageDuration_sec && timerCooldown_sec == sabotageCooldown_sec) {
            Thread timerThread = new Thread(() -> {
                while (timerCooldown_sec > 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;  // Exit the loop if the thread is interrupted
                    }
                    timerCooldown_sec--;
                    cooldownChange.newTime(timerCooldown_sec);
                    if (timerDuration_sec > 0) {
                        timerDuration_sec--;
                        durationChange.newTime(timerDuration_sec);
                    }
                    System.out.println("Sabotage timer: " + timerDuration_sec + "s");
                    System.out.println("Sabotage cooldown: " + timerCooldown_sec + "s");
                }
                System.out.println("Sabotage completed.");
            });
            timerThread.start();
        }
    }
}
