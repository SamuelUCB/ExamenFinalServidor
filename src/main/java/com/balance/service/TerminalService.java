package com.balance.service;

import com.balance.model.Terminal;

/**
 * Created by KEVIN on 26/05/2017.
 */
public interface TerminalService {

    void saveTerminal(Terminal Terminal);
    Iterable<Terminal> listAllTerminals();
    Terminal getTerminalById(Integer id);
    void deleteTerminal(Integer id);

    void setActiveTerminalById(Integer id);
    Terminal getTerminalBySerial(int serial);

    void saveTerminalEdited(Terminal terminalNew,Terminal terminalOld);
}
