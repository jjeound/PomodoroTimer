package com.pomodoro.timer.presentation.common

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.tooling.preview.Preview
import com.pomodoro.timer.ui.theme.MyTheme
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RainDropGlassEffect(modifier: Modifier = Modifier) {
    val shaderCode = """
uniform float2 iResolution;
uniform float iTime;

float hash(float n) { return fract(sin(n) * 43758.5453123); }
float noise(float2 x) {
    float2 p = floor(x);
    float2 f = fract(x);
    f = f*f*(3.0-2.0*f);
    float n = p.x + p.y*57.0;
    return mix(mix(hash(n+0.0), hash(n+1.0), f.x),
               mix(hash(n+57.0), hash(n+58.0), f.x), f.y);
}

// 💧 빗방울 하나 (둥글게 표현)
float dropShape(float2 uv, float size) {
    float d = length(uv);
    return smoothstep(size, size * 0.5, d);
}

// 빗방울 레이어 (화면에 맺히거나 천천히 흐르는)
float rainLayer(float2 uv, float time, float scale, float speed) {
    uv *= scale;

    float2 id = floor(uv);
    float2 f = fract(uv);

    float n = noise(id);
    float dropSize = mix(0.008, 0.02, fract(n * 5.0)); // 크기 랜덤
    float offset = hash(id.x + id.y * 10.0);

    // ⏬ 떨어지는 속도 (느리게)
    float fall = fract(f.y - time * speed - offset);

    // 물방울 위치
    float2 dropUv = f - float2(0.5, fall);

    // 💧 모양 만들기
    float drop = dropShape(dropUv, dropSize);

    // 일부는 멈춰있는 물방울 (유리에 맺힌 느낌)
    float stillDrop = dropShape(f - float2(0.5, 0.3), dropSize * 0.8) * step(0.98, fract(n * 10.0));

    return max(drop, stillDrop);
}

half4 main(float2 fragCoord) {
    float2 uv = fragCoord / iResolution.xy;
    uv.y = 1.0 - uv.y;
    uv.x *= iResolution.x / iResolution.y;

    // 여러 층의 비 (멀리, 가까이)
    float rain = 0.0;
    rain += rainLayer(uv + float2(0.0, 0.0), iTime, 15.0, 0.15);
    rain += rainLayer(uv + float2(0.3, 0.1), iTime, 25.0, 0.25);
    rain += rainLayer(uv + float2(-0.2, 0.3), iTime, 35.0, 0.35);

    rain = clamp(rain, 0.0, 1.0);

    // 💧물방울 반짝임 (빛 반사 효과)
    float3 rainColor = float3(0.8, 0.9, 1.0) * rain * 1.2;

    // 배경은 투명 (혹은 살짝 어두운 유리 느낌)
    float3 bg = float3(0.0, 0.0, 0.0);

    float alpha = rain * 0.9;
    float3 color = mix(bg, rainColor, rain);

    return half4(color, alpha);
}
"""

    val shader = RuntimeShader(shaderCode)
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            time += 0.016f
            delay(16L)
        }
    }

    Canvas(modifier = modifier) {
        shader.setFloatUniform("iResolution", size.width, size.height)
        shader.setFloatUniform("iTime", time)
        drawRect(brush = ShaderBrush(shader), size = size)
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview
@Composable
fun RainEffectPreview() {
    MyTheme {
        RainDropGlassEffect(
            modifier = Modifier
        )
    }
}