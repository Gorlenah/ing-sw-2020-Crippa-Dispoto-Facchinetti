package it.polimi.ingsw.model.parser;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.controller.Step;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.cards.effects.basic.BasicTurn;
import it.polimi.ingsw.model.cards.effects.basic.BlockUnder;
import it.polimi.ingsw.model.cards.effects.basic.CantLevelUp;
import it.polimi.ingsw.model.cards.effects.remove.RemoveBlock;
import it.polimi.ingsw.model.cards.effects.build.DomeEverywhere;
import it.polimi.ingsw.model.cards.effects.build.ExtraBlockAbove;
import it.polimi.ingsw.model.cards.effects.build.ExtraBlockPerimetral;
import it.polimi.ingsw.model.cards.effects.move.*;
import it.polimi.ingsw.model.cards.effects.win.JumpEffect;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Deserializer for DivinityEffectReader
 */
public class DivinityEffectDeserializer implements JsonDeserializer<Map<String,Turn>> {
    /**
     * Method used by Gson to deserialize correctly JSON file
     * @param jsonElement json to deserialize
     * @param type type of object
     * @param jsonDeserializationContext Context for deserialization that is passed to a custom deserializer during invocation of its method
     * @return association between card name and effect
     * @throws JsonParseException Exception type for parsing problems, used when non-well-formed content is encountered
     */
    @Override
    public Map<String, Turn> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
       Map<String, Turn> map = new HashMap<>();
        JsonArray jsonArray = jsonElement.getAsJsonObject().get("cardsEffects").getAsJsonArray();
        Turn t = null;
        for(JsonElement jsonE : jsonArray){
            String cardName = jsonE.getAsJsonObject().get("cardName").getAsString();
            Type listType = new TypeToken<List<Step>>() {}.getType();
            List<Step> turnStructure = new Gson().fromJson(jsonE.getAsJsonObject().get("Effect").getAsJsonObject().get("turnStructure"),listType);

            switch(jsonE.getAsJsonObject().get("Effect").getAsJsonObject().get("effectName").getAsString()){
                case "ExtraMove":
                    t = new ExtraMove(jsonE.getAsJsonObject().get("Effect").getAsJsonObject().get("numberOfExtraMoves").getAsInt(),turnStructure);
                    break;
                case "BlockUnder":
                    t = new BlockUnder(turnStructure);
                    break;
                case "SwitchCharacter":
                    t = new SwitchCharacter(turnStructure);
                    break;
                case "NoMoveUp":
                    t = new NoMoveUp(turnStructure);
                    break;
                case "DomeEverywhere":
                    t = new DomeEverywhere(turnStructure);
                    break;
                case "ExtraBlockAbove":
                    t = new ExtraBlockAbove(jsonE.getAsJsonObject().get("Effect").getAsJsonObject().get("buildInSameCell").getAsBoolean(),jsonE.getAsJsonObject().get("Effect").getAsJsonObject().get("blocksLeft").getAsInt(), turnStructure);
                    break;
                case "PushCharacter":
                    t = new PushCharacter(turnStructure);
                    break;
                case "JumpEffect":
                    t = new JumpEffect(turnStructure);
                    break;
                case "BasicTurn":
                    t = new BasicTurn();
                    break;
                case "ExtraMovePerimeter":
                    t = new ExtraMovePerimeter(turnStructure);
                    break;
                case "ExtraBlockPerimetral":
                    t = new ExtraBlockPerimetral(jsonE.getAsJsonObject().get("Effect").getAsJsonObject().get("blocksLeft").getAsInt(), turnStructure);
                    break;
                case "RemoveBlock":
                    t = new RemoveBlock(turnStructure);
                    break;
                case "CantLevelUp":
                    t = new CantLevelUp(turnStructure);
                    break;
            }
            map.put(cardName,t);
        }

        return map;
    }
}
