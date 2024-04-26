package at.fhv.withthem.MeetingLogic.task;

import at.fhv.withthem.MeetingLogic.EmergencyMeetingMessage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TaskFileDownloadUpload extends Task {
    private String _state;
    private float _progress;
    private boolean _running;
    private ScheduledExecutorService executorService;
    private ScheduledFuture<?> _future;

    public TaskFileDownloadUpload(int id) {
        super("FileDownloadUpload", id);
        reset();
        executorService = Executors.newScheduledThreadPool(1);
    }

    @Override
    public void playerAction(EmergencyMeetingMessage msg, Reaction reaction) {
        IncomingFileDownloadUploadMessage msg_fdu = (IncomingFileDownloadUploadMessage) msg;
        if (msg_fdu.getMake().equals("Download") && !_running) {
            _running = true;
            final Runnable download = new Runnable() {
                @Override
                public void run() {
                    _progress += 0.1f;
                    reaction.react();
                }
            };
            _future = executorService.scheduleAtFixedRate(download, 0, 1, TimeUnit.SECONDS);
            executorService.schedule(new Runnable() {
                @Override
                public void run() {
                    if (_progress >= 1.0f) {
                        _state = "Upload";
                        _progress = -1.0f;
                        _running = false;
                        reaction.react();
                    }
                    _future.cancel(true);
                }
            }, 9, TimeUnit.SECONDS);
        } else if (msg_fdu.getMake().equals("openFileUpload")) {
            _state = "Upload";
            _progress = 0.0f;
            reaction.react();
        } else if (msg_fdu.getMake().equals("Upload") && !_running) {
            _running = true;
            final Runnable download = new Runnable() {
                @Override
                public void run() {
                    _progress += 0.1f;
                    reaction.react();
                }
            };
            _future = executorService.scheduleAtFixedRate(download, 0, 1, TimeUnit.SECONDS);
            executorService.schedule(new Runnable() {
                @Override
                public void run() {
                    if (_progress >= 1.0f) {
                        _future.cancel(true);
                        _running = false;
                    }
                }
            }, 9, TimeUnit.SECONDS);
        }
    }

    @Override
    public void reset() {
        if (_future != null)
            _future.cancel(true);
        _state = "Download";
        _progress = 0.0f;
        _running = false;
    }

    @Override
    public boolean taskCompleted() {
        return (_state.equals("Upload") && _progress >= 1.0f);
    }

    @Override
    public EmergencyMeetingMessage getCurrentState() {
        return new OutgoingFileDownloadUploadMessage(_state, _progress);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Task task) {
            return task.getType().equals(this.getType());
        } else if (obj instanceof String type) {
            return type.equals("File Download") || type.equals("File Upload");
        }

        return false;
    }
}
