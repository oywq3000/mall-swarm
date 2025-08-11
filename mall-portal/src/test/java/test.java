import com.oyproj.mall.model.UmsMember;
import com.oyproj.portal.dto.UmsUpdateMemberInfoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class test {


    @Test
    public void TestBeancopy(){
        UmsMember umsMember = new UmsMember();
        umsMember.setId(1l);
        umsMember.setNickname("欧阳1");
        umsMember.setPhone("19808018616");
        UmsUpdateMemberInfoDto umsUpdateMemberInfoDto = new UmsUpdateMemberInfoDto();
        umsUpdateMemberInfoDto.setGender(1);
        BeanUtils.copyProperties(umsUpdateMemberInfoDto,umsMember);
        System.out.println(umsMember);
    }

}
