package com.abhspatil.distributedlock;

import com.abhspatil.distributedlock.lock.DistributedLockUtil;
import io.reactivex.rxjava3.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emp")
public class EmpLockController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DistributedLockUtil lockUtil;

    @GetMapping("/{id}")
    public Employee getEmp(@PathVariable("id") Integer id){
        return employeeRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Employee createEmp(@RequestBody Employee employee){
        System.out.println(employee);
        return employeeRepository.save(employee);
    }

    @PutMapping
    public Employee updateEmp(@RequestBody Employee employee){

        try {
            if(!lockUtil.tryLock(employee.getId().toString())) return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        while (!lockUtil.tryLock(employee.getId().toString())){
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            System.out.println("inside loop waiting to get lock");
//        }

        Employee employee1= update(employee);
        lockUtil.unlock(employee.getId().toString());
        return employee1;
    }

    public Employee update(Employee employee){
        Employee nEmp = employeeRepository.findById(employee.getId()).orElse(null);
        assert nEmp != null;
        nEmp.setSalary(nEmp.getSalary()+ employee.getSalary());
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return employeeRepository.save(nEmp);
    }
}
