package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @Test
    void testMember(){
        // given
        Member member = new Member("Kim");

        // when
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        // then
        assertThat(savedMember.getId()).isEqualTo(findMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(findMember.getUsername());
        assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);

    }

    @Test
    void findByUsernameAndAge() {
        Member member1 = new Member("Kim", 10);
        Member member2 = new Member("Kim", 15);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("Kim", 10);

        assertThat(result.get(0).getUsername()).isEqualTo("Kim");
        assertThat(result.get(0).getAge()).isEqualTo(15);
    }

    @Test
    void namedQuery() {
        Member member1 = new Member("Kim", 10);
        Member member2 = new Member("Lee", 15);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsername("Kim");

        assertThat(result.get(0).getUsername()).isEqualTo("Kim");
        assertThat(result.get(0).getAge()).isEqualTo(10);
    }

    @Test
    void testQuery() {
        Member member1 = new Member("Kim", 10);
        Member member2 = new Member("Lee", 15);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findMember("Kim", 10);

        assertThat(result.get(0)).isEqualTo(member1);
        assertThat(result.get(0).getUsername()).isEqualTo("Kim");
        assertThat(result.get(0).getAge()).isEqualTo(10);
    }

    @Test
    void findUsernameList() {
        Member member1 = new Member("Kim", 10);
        Member member2 = new Member("Lee", 15);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }

    @Test
    void findMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member1 = new Member("Kim", 10);
        member1.changeTeam(team);
        memberRepository.save(member1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    void findUsernameByList() {
        Member member1 = new Member("Kim", 10);
        Member member2 = new Member("Lee", 15);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> usernameList = memberRepository.findByNames(List.of("Kim", "Lee"));
        for (Member member : usernameList) {
            System.out.println("member = " + member);
        }
    }

    @Test
    void returnType() {
        Member member1 = new Member("Kim", 10);
        Member member2 = new Member("Lee", 15);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> kim = memberRepository.findListByUsername("Kim");
        Member kim1 = memberRepository.findMemberByUsername("Kim");
        Optional<Member> kim2 = memberRepository.findOptionalByUsername("kim");
        System.out.println("kim1 = " + kim2.get());
    }
}