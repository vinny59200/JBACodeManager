package platform.vv.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import platform.vv.model.VVCode;
import platform.vv.service.VVCodeService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@RestController
@Slf4j
public class CodeSharingController {

    @Autowired
    VVCodeService vvCodeService;

    @GetMapping(value = "/code", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getCodeHTMLPage() {
        log.error("getCodeHTMLPage");
        VVCode vvCode = getVvCode();

        return "<html>\n" + "<header> <title>Code</title><link rel=\"stylesheet\"\n" +
                "       href=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css\">\n" +
                "<script src=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js\"></script>\n" +
                "<script>hljs.initHighlightingOnLoad();</script></header>\n" +
                "<body>\n" + " <pre id=\"code_snippet\"><code>\n" +
                vvCode.getContent() +
                "</code></pre>\n" + "<span id=\"load_date\">" + dateToString(vvCode.getDate()) + "</span></body>\n" + "</html>";
    }

    private VVCode getVvCode() {
        List<VVCode> list = vvCodeService.getAllVVCode();
        VVCode vvCode = null;
        if (list.size() == 0) {
            log.error("VV6 list empty");
            vvCode = new VVCode();
            vvCode.setContent("public static void main(String[] args) {}");
            vvCode.setDate(LocalDateTime.now());
        } else {
            log.error("VV6 list not empty");
            vvCode = list.get(list.size() - 1);
        }
        return vvCode;
    }

    @GetMapping(value = "/api/code", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getCodeJSON() {
        log.error("getCodeJSON");
        VVCode vvCode = getVvCode();
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", vvCode.getContent());
        map.put("date", dateToString(vvCode.getDate()));
        map.put("time", vvCode.getTime());
        map.put("views", vvCode.getViews());
        return map;
    }

    @PostMapping(value = "/api/code/new", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> createNewCodeSnippet(@RequestBody Map<String, String> data) {
        log.error("createNewCodeSnippet");
        log.error(data.toString());
        VVCode vvCode = new VVCode();
        vvCode.setContent(data.get("code"));
        vvCode.setTime(Integer.parseInt(data.get("time")));
        vvCode.setViews(Integer.parseInt(data.get("views")));
        LocalDateTime dateTime = stringToDate(data.get("date"));
        vvCode.setDate(dateTime);
        vvCode = vvCodeService.saveOrUpdate(vvCode);
        HashMap<String, String> map = new HashMap<>();
        map.put("id", vvCode.getId().toString());
        return map;
    }

    private LocalDateTime stringToDate(String data) {
        if (data == null) {
            return LocalDateTime.now();
        }
        String str = data;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        return dateTime;
    }

    private String dateToString(LocalDateTime data) {
        if (data == null) {
            return "";
        }
        LocalDateTime date = data;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String str = date.format(formatter);
        return str;
    }

    @GetMapping(value = "/code/new", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getCodeHTMLInputPage() {
        log.error("getCodeHTMLInputPage");
        return "<html>\n" + "<header> <title>Create</title><script>function send() {\n" +
                "    let object = {\n" +
                "        \"code\": document.getElementById(\"code_snippet\").value,\n" +
                "        \"time\": document.getElementById(\"time_restriction\").value,\n" +
                "        \"views\": document.getElementById(\"views_restriction\").value\n" +
                "    };\n" +
                "    \n" +
                "    let json = JSON.stringify(object);\n" +
                "    \n" +
                "    let xhr = new XMLHttpRequest();\n" +
                "    xhr.open(\"POST\", '/api/code/new', false)\n" +
                "    xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');\n" +
                "    xhr.send(json);\n" +
                "    \n" +
                "    if (xhr.status == 200) {\n" +
                "      alert(\"Success!\");\n" +
                "    }\n" +
                "}</script>/header>\n" +
                "<body>\n" + "<input id=\"time_restriction\" type=\"text\"/><input id=\"views_restriction\" type=\"text\"/><textarea id=\"code_snippet\"> ... </textarea>" +
                "<button id=\"send_snippet\" type=\"submit\" onclick=\"send()\">Submit</button>\n" + "</html>";
    }

    @GetMapping(value = "/api/code/{vvcodeid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getCodeJSONById(@PathVariable("vvcodeid") String vvcodeid) {
        log.error("getCodeJSONById: " + vvcodeid);
        VVCode vvCode = vvCodeService.getVVCode(vvcodeid);
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", vvCode.getContent());
        map.put("date", dateToString(vvCode.getDate()));
        if(vvCode.getViews() ==1 || vvCode.getTime() == 1) {
            throw new NotFoundException();
        }
        map.put("time", recalculateTime(vvCode) );
        map.put("views", recalculateViews(vvCode));
        return map;
    }
    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not found")
    private  class NotFoundException extends RuntimeException {
    }
    private Integer recalculateViews(VVCode vvCode) {

        if (vvCode.getViews() == 0) {
            return 0;
        } else if(vvCode.getViews() == -1) {
            throw new NotFoundException();
        }
        if(vvCode.getViews() == 1) {
            vvCode = vvCode.toBuilder().views(- 1).build();
            vvCodeService.saveOrUpdate(vvCode);
            return 0;
        }
        vvCode = vvCode.toBuilder().views(vvCode.getViews() - 1).build();
        vvCodeService.saveOrUpdate(vvCode);
        return vvCode.getViews();
    }

    private Integer recalculateTime(VVCode vvCode) {
        Integer time= vvCode.getTime();
        long toSeconds=Duration.between(vvCode.getDate(), LocalDateTime.now()).toSeconds();
        log.error("recalculateTime: " + time + " " + toSeconds);
        int result=0;
        if(time == 0) {
            return 0;
        }else  {
            result = time - (int) toSeconds;
            if(result <= 0) {
                throw new NotFoundException();
            }
        }
        return result;
    }

    @GetMapping(value = "/code/{vvcodeid}", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getCodeHTMLPageById(@PathVariable("vvcodeid") String vvcodeid) {
        log.error("getCodeHTMLPageById");
        VVCode vvCode = vvCodeService.getVVCode(vvcodeid);

        String result= "<html>\n" + "<header> <title>Code</title><link rel=\"stylesheet\"\n" +
                "       href=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css\">\n" +
                "<script src=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js\"></script>\n" +
                "<script>hljs.initHighlightingOnLoad();</script></header>\n" +
                "<body>\n" + " <pre id=\"code_snippet\"><code>\n" +
                vvCode.getContent() +
                "</code></pre>\n" + "<span id=\"load_date\">" + dateToString(vvCode.getDate()) + "</span>";
                if(vvCode.getTime()!=0)result+="<span id=\"time_restriction\">"+recalculateTime(vvCode)+"</span>";
                if(vvCode.getViews()!=0)result+="<span id=\"views_restriction\">"+recalculateViews(vvCode)+"</span>";
                result+="</body>\n" + "</html>";
        return result;
    }

    @GetMapping(value = "/api/code/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object>[] getCodeJSONLatest() {
        log.error("getCodeJSONLatest");
        List<Map<String, Object>> result = new ArrayList<>();
        vvCodeService.getAllVVCode().stream().sorted(Comparator.comparing(VVCode::getDate).reversed()).limit(10).forEach(
                vvCode -> {

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("code", vvCode.getContent());
                    map.put("date", dateToString(vvCode.getDate()));
                    map.put("time", vvCode.getTime());
                    map.put("views", vvCode.getViews());
                    result.add(map);
                });
        return result.toArray(new Map[result.size()]);
    }

    @GetMapping(value = "/code/latest", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getCodeHTMLPageLatest() {
        log.error("getCodeHTMLPageLatest");
        List<String> result = new ArrayList<>();
        result.add("<html>\n" + "<header> <title>Latest</title><link rel=\"stylesheet\"\n" +
                "       href=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css\">\n" +
                "<script src=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js\"></script>\n" +
                "<script>hljs.initHighlightingOnLoad();</script></header>\n" +
                "<body>\n");
        vvCodeService.getAllVVCode().stream().sorted(Comparator.comparing(VVCode::getDate).reversed()).limit(10).forEach(
                vvCode -> {
                    result.add(" <pre id=\"code_snippet\"><code>\n" +
                            vvCode.getContent() +
                            "</code></pre>\n" + "<span id=\"load_date\">" + dateToString(vvCode.getDate()) + "</span>");
                });

        return result+ "</body>\n" + "</html>";
    }

}
