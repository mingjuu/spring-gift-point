package gift.domain;

import gift.utils.TimeStamp;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Member extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String kakaoId;

    @OneToMany(mappedBy = "member")
    private List<Wish> wishList = new ArrayList<>();

    public Member() {
    }

    private Member(Builder builder) {
        this.email = builder.email;
        this.password = builder.password;
        this.kakaoId = builder.kakaoId;
    }

    public static class Builder {
        private String email;
        private String password;
        private String kakaoId;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder kakaoId(String kakaoId) {
            this.kakaoId = kakaoId;
            return this;
        }

        public Member build() {
            return new Member(this);
        }
    }

    public Long getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getKakaoId() {
        return kakaoId;
    }
    public List<Wish> getWishList() {
        return wishList;
    }

}