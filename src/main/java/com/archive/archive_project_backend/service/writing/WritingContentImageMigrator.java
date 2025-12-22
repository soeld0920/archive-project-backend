package com.archive.archive_project_backend.service.writing;

import com.archive.archive_project_backend.infra.storage.LocalFileStorage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WritingContentImageMigrator {
    private final LocalFileStorage localFileStorage;
    private final ObjectMapper objectMapper;

    //글의 tmp/...을 파일명만으로 반환
    private String extractTempKey(String src) {
        return Paths.get(src).getFileName().toString();
    }

    //변환 함수
    public JsonNode migrateTmpImages(JsonNode root, String targetDir) throws IOException {
        // 중복 이미지(같은 tempKey가 여러 번 등장) 대비
        Map<String, String> movedCache = new HashMap<>();
        walkAndReplace(root, targetDir, movedCache);
        return root;
    }

    private void walkAndReplace(JsonNode node,
                                String targetDir,
                                Map<String, String> movedCache) throws IOException {

        if (node == null) return;

        if (node.isObject()) {
            ObjectNode obj = (ObjectNode) node;

            // 1) 이미지 노드인지 검사
            String type = obj.path("type").asText(null);
            if ("image".equals(type)) {
                //2. 이미지 노드의 특성 필드 추출
                JsonNode attrsNode = obj.path("attrs");
                if (attrsNode.isObject()) {
                    ObjectNode attrs = (ObjectNode) attrsNode;
                    //3. 이미지의 경로 추출
                    JsonNode srcNode = attrs.get("src");

                    if (srcNode != null && srcNode.isTextual()) {
                        String src = srcNode.asText();
                        //4. 이미지 검사
                        if (src.startsWith("/files/tmp/")) {
                            String tempKey = extractTempKey(src);

                            // 같은 tempKey가 여러 번 나오면 한 번만 move
                            String newUrl = movedCache.get(tempKey);
                            if (newUrl == null) {
                                newUrl = localFileStorage.moveFromTmp(tempKey, targetDir);
                                movedCache.put(tempKey, newUrl);
                            }

                            // 2) src 치환
                            attrs.put("src", newUrl);
                        }
                    }
                }
            }

            // 3) 하위 필드 순회
            Iterator<Map.Entry<String, JsonNode>> fields = obj.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                walkAndReplace(entry.getValue(), targetDir, movedCache);
            }

        } else if (node.isArray()) {
            ArrayNode arr = (ArrayNode) node;
            for (JsonNode child : arr) {
                walkAndReplace(child, targetDir, movedCache);
            }
        }
        // text/number 등 leaf는 무시
    }
}
