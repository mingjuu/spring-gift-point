package gift.controller;

import gift.dto.request.MemberRequest;
import gift.dto.response.KakaoLoginResponse;
import gift.dto.response.TokenResponse;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    @Operation(summary = "회원 가입", description = "새 회원을 등록하고 토큰을 받는다.")
    public ResponseEntity<TokenResponse> register(@RequestBody MemberRequest memberRequest) {
        TokenResponse responseBody = memberService.handleNormalRegister(memberRequest);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + responseBody.getToken())
                .body(responseBody);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "회원을 인증하고 토큰을 받는다.")
    public ResponseEntity<TokenResponse> login(@RequestBody MemberRequest memberRequest) {
        TokenResponse responseBody = memberService.handleNormalAuthenticate(memberRequest);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + responseBody.getToken())
                .body(responseBody);
    }

    @GetMapping("/kakao/login")
    @Operation(summary = "카카오 로그인 콜백", description = "카카오 API로 회원을 인증하고 토큰을 받는다.")
    public ResponseEntity<KakaoLoginResponse> kakaoCallback(@RequestParam String code) {
        KakaoLoginResponse response = memberService.handleKakaoLogin(code);
        return ResponseEntity.ok(response);

    }
}