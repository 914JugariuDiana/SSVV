package com.ssvv.validation;

import com.ssvv.domain.Student;
import com.ssvv.repository.StudentXMLRepo;

public class StudentValidator implements Validator<Student> {

    /**
     * Valideaza un student
     * @param entity - studentul pe care il valideaza
     * @throws ValidationException - daca studentul nu e valid
     */

    @Override
    public void validate(Student entity) throws ValidationException {
        if(entity.getID().equals("")){
            throw new ValidationException("Id incorect!");
        }
        if(entity.getID() == null){
            throw new ValidationException("Id incorect!");
        }
        try {
            Integer id = Integer.parseInt(entity.getID());
            if (id <= 0 || id > 10000) throw new ValidationException("Id incorect!");
        } catch (NumberFormatException e) {
            throw new ValidationException("Id incorect!");
        }
        if (entity.getNume() == null) {
            throw new ValidationException("Nume incorect!");
        }
        if(entity.getNume() == ""){
            throw new ValidationException("Nume incorect!");
        }
        if(!entity.getNume().matches("[A-Za-z ]+")) {
            throw new ValidationException("Nume incorect!");
        }
        if(entity.getGrupa() <= 0 || entity.getGrupa() > 10000) {
            throw new ValidationException("Grupa incorecta!");
        }
        if(entity.getEmail() == null){
            throw new ValidationException("Email incorect!");
        }
        if(!entity.getEmail().matches(".+@.+")){
            throw new ValidationException("Email incorect!");
        }
        if(entity.getEmail().equals("")){
            throw new ValidationException("Email incorect!");
        }
    }
}
