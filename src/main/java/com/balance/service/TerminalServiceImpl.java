package com.balance.service;

import com.balance.model.Terminal;
import com.balance.model.User;
import com.balance.repository.TerminalRepository;
import com.balance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;

/**
 * Created by KEVIN on 26/05/2017.
 */
@Service("TerminalService")
public class TerminalServiceImpl implements TerminalService {

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private UserRepository userRepository;



    @Override
    public void saveTerminal(Terminal Terminal) {
        terminalRepository.save(Terminal);
    }

    @Override
    public Iterable<Terminal> listAllTerminals() {
        return terminalRepository.findAll();
    }

    @Override
    public Terminal getTerminalById(Integer id) {
        return terminalRepository.findOne(id);
    }

    @Override
    public void deleteTerminal(Integer id) {
        terminalRepository.delete(id);
    }

    @Override
    public void setActiveTerminalById(Integer id){
        if(terminalRepository.findOne(id).isActive()){
            terminalRepository.findOne(id).setActive(false);
        }else{
            terminalRepository.findOne(id).setActive(true);
        }

    }

    @Override
    public Terminal getTerminalBySerial(int serial){
        Iterator<Terminal> iterator=terminalRepository.findAll().iterator();
        while(iterator.hasNext()){
            Terminal auxT = iterator.next();
            if(serial==auxT.getSerial()) {
                return auxT;
            }
        }
        return null;
    }

    @Override
    public void saveTerminalEdited(Terminal terminalNew,Terminal terminalOld){
       /* Iterator<User> iterator=userRepository.findAll().iterator();
        while(iterator.hasNext()){
            User auxU = iterator.next();
            if(terminalOld.getSerial()==auxU.getTerminal().getSerial()) {
                auxU.setTerminal(terminalNew);
                auxU.setBand(terminalNew.getBandModel().getName());
                userRepository.save(auxU);
                break;
            }
        }
        terminalRepository.delete(terminalOld.getSerial());
        terminalRepository.save(terminalNew);*/
    }
}
