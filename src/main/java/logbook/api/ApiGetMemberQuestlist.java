package logbook.api;

import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonValue.ValueType;

import logbook.bean.Quest;
import logbook.bean.QuestCollection;
import logbook.internal.JsonHelper;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_get_member/questlist
 *
 */
@API("/kcsapi/api_get_member/questlist")
public class ApiGetMemberQuestlist implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getJsonObject("api_data");
        //        System.out.println(data);
        //        System.out.println("exec count:" + data.getInt("api_exec_count"));
        if (data != null) {
            // 任務
            JsonArray arr = data.getJsonArray("api_list");
            if (arr.stream().anyMatch(q -> q.getValueType() == ValueType.NUMBER)) {
                JsonArrayBuilder jab = Json.createArrayBuilder();
                arr.stream().filter(q -> q.getValueType().equals(ValueType.OBJECT)).forEach(q -> jab.add(q));
                arr = jab.build();
            }
            Map<Integer, Quest> quest = JsonHelper.toMap(arr, Quest::getNo, Quest::toQuest);
            QuestCollection.get().addQuest(quest, data.getInt("api_disp_page") * data.getInt("api_page_count"));
        }
    }

}
