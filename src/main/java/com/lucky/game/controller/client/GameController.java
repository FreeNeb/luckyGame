package com.lucky.game.controller.client;

import com.lucky.game.biz.GameBiz;
import com.lucky.game.dto.client.QueryGameDto;
import com.lucky.game.core.constant.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author conan
 *         2018/1/8 17:27
 **/

@RestController
@Api(value = "order", description = "游戏接口API")
@RequestMapping(value = "/api/client/game", produces = "application/json;charset=UTF-8")
public class GameController {

    @Autowired
    private GameBiz gameBiz;

    /**
     * 查询有效游戏列表
     */
    @RequestMapping(value = "/findAllEffectGame", method = RequestMethod.POST)
    @ApiOperation(value = "查询有效游戏列表", notes = "", httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(name = "dto", value = "查询参数", required = true, paramType = "body", dataType = "QueryGameDto")})
    @ResponseBody
    public ResponseData findAllEffectGame(@Valid @RequestBody QueryGameDto dto) {
        return gameBiz.findAllEffectGame(dto);
    }
}
