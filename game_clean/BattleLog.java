import java.util.ArrayList;
import java.util.List;

public class BattleLog {
    private List<String> records = new ArrayList<>();

    public void addRecord(String text) {
        records.add(text);
    }

    public List<String> getRecords() {
        return records;
    }
}
