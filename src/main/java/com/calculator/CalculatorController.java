package com.calculator;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CalculatorController {

    private final CalculatorEngine engine = new CalculatorEngine();
    private final MemoryManager memory = new MemoryManager();

    @PostMapping("/digit")
    public Map<String, Object> inputDigit(@RequestBody Map<String, Integer> body) {
        engine.inputDigit(body.get("digit"));
        return buildResponse();
    }

    @PostMapping("/decimal")
    public Map<String, Object> inputDecimal() {
        engine.inputDecimal();
        return buildResponse();
    }

    @PostMapping("/operator")
    public Map<String, Object> inputOperator(@RequestBody Map<String, String> body) {
        engine.setOperator(body.get("operator"));
        return buildResponse();
    }

    @PostMapping("/equals")
    public Map<String, Object> inputEquals() {
        engine.equals();
        return buildResponse();
    }

    @PostMapping("/clear")
    public Map<String, Object> clear() {
        engine.clear();
        return buildResponse();
    }

    @PostMapping("/backspace")
    public Map<String, Object> backspace() {
        engine.backspace();
        return buildResponse();
    }

    @PostMapping("/negate")
    public Map<String, Object> negate() {
        engine.negate();
        return buildResponse();
    }

    @PostMapping("/percent")
    public Map<String, Object> percent() {
        engine.percent();
        return buildResponse();
    }

    @PostMapping("/memory/clear")
    public Map<String, Object> memoryClear() {
        memory.memoryClear();
        return buildResponse();
    }

    @PostMapping("/memory/recall")
    public Map<String, Object> memoryRecall() {
        if (memory.hasMemory()) {
            engine.clear();
            engine.setCurrentValue(memory.getMemoryValue());
        }
        return buildResponse();
    }

    @PostMapping("/memory/add")
    public Map<String, Object> memoryAdd() {
        memory.memoryAdd(engine.getCurrentValue());
        return buildResponse();
    }

    @PostMapping("/memory/subtract")
    public Map<String, Object> memorySubtract() {
        memory.memorySubtract(engine.getCurrentValue());
        return buildResponse();
    }

    @PostMapping("/memory/store")
    public Map<String, Object> memoryStore() {
        memory.memoryStore(engine.getCurrentValue());
        return buildResponse();
    }

    private Map<String, Object> buildResponse() {
        Map<String, Object> response = new HashMap<>();
        double value = engine.getCurrentValue();

        if (Double.isInfinite(value)) {
            response.put("display", "Cannot divide by zero");
        } else if (Double.isNaN(value)) {
            response.put("display", "Error");
        } else if (value == (long) value && Math.abs(value) < 1e15) {
            response.put("display", String.valueOf((long) value));
        } else {
            String formatted = String.valueOf(value);
            if (formatted.length() > 16) {
                formatted = String.format("%.10g", value);
            }
            response.put("display", formatted);
        }

        response.put("hasMemory", memory.hasMemory());
        response.put("operator", engine.getOperator());
        return response;
    }
}
