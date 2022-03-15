package platform.vv.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import platform.vv.model.VVCode;
import platform.vv.repository.VVCodeRepository;

@Service
@Slf4j
public class VVCodeService {

    @Autowired
    VVCodeRepository vvCodeRepository;

    public List<VVCode> getAllVVCode() {
        List<VVCode> vvCodeList = new ArrayList<>();
        vvCodeRepository.findAll(Sort.by(Sort.Direction.DESC, "date")).forEach(vvCodeList::add);
        vvCodeList= vvCodeList.stream().filter(vvCode -> vvCode.getTime() == 0 && vvCode.getViews() == 0).collect(Collectors.toList());
        log.error("VV54 vvCodeList: " + vvCodeList);
        return vvCodeList;
    }

    public VVCode saveOrUpdate(VVCode vvCode) {
//        if(vvCode.getId()!=0 && this.getVVCode(vvCode.getId())!=null) {
//            log.error("VV5 id to delete: " + vvCode.getId());
//            this.delete(vvCode.getId());
//        }
//        if (vvCode.getId() == null) {
//            int newId = generateNewIdCuzFuckingAutoIncrementFoulsUp();
//            vvCode = vvCode.toBuilder().id(newId).build();
//        }
        log.error("VV2 saveOrUpdate" + vvCode);
        vvCodeRepository.save(vvCode);
        return vvCode;
    }

//    private int generateNewIdCuzFuckingAutoIncrementFoulsUp() {
//        int newId = -1;
//        List<VVCode> vvCodeList = new ArrayList<>();
//        vvCodeRepository.findAll().forEach(vvCodeList::add);
//        for (VVCode vvCode : vvCodeList) {
//            if (vvCode.getId() > newId) {
//                newId = vvCode.getId();
//            }
//        }
//        newId++;
//        return newId;
//    }

    public void delete(String id) {
        vvCodeRepository.deleteById(id);
    }

    public VVCode getVVCode(String id) {
        return vvCodeRepository.findById(id).orElseGet(()-> VVCode.builder().build());
    }

}
