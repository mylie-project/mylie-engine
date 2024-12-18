package mylie.input;

import java.util.List;
import mylie.async.Result;

public interface InputProvider {
    Result<List<InputEvent>> inputEvents();
}
