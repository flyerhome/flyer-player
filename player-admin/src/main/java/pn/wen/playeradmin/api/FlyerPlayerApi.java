package pn.wen.playeradmin.api;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import pn.wen.playeradmin.common.FlyerResult;
import pn.wen.playeradmin.dto.FlyerDownloadDto;
import pn.wen.playeradmin.service.FlyerPlayerService;

@RestController
@RequestMapping("/player")
public class FlyerPlayerApi {
    @Resource
    private FlyerPlayerService playerService;
    //先下载在服务器 在播放本地，这个会播放更快
    @PostMapping("/download")
    public FlyerResult<?> download(@RequestBody FlyerDownloadDto dto) {
        return playerService.download(dto);
    }

    @GetMapping("/list")
    public FlyerResult<?> list() {
        return playerService.list();
    }
}
