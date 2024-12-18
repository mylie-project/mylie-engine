package mylie.input;

import mylie.async.Result;

import java.util.List;

public interface InputProvider {
    Result<List<InputEvent>> inputEvents();
}
