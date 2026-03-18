package pn.wen.playeradmin.api;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pn.wen.playeradmin.service.FlyerPlayerService;

@RestController
@RequestMapping("/video/play")
public class VideoPlayApi {

    @Resource
    private FlyerPlayerService playerService;


    /**
     * 视频分片播放接口
     * @param fileName 视频文件名（如：test.mp4）
     * @param request 请求对象
     * @return 视频分片数据流
     */
    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> fileName(@PathVariable String fileName, HttpServletRequest request) {
        return playerService.fileName(fileName, request);
    }



}
