package com.fullstack.booking.repository;

import com.fullstack.booking.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import javax.persistence.LockModeType;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    List<TimeSlot> findByServiceIdAndIsAvailableTrue(Long serviceId);

    /**
     * 悲观锁读取时段，供下单时串行化同一时段的并发扣减，避免超卖。
     * 必须在事务中调用。
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from TimeSlot t where t.id = :id")
    Optional<TimeSlot> findByIdForUpdate(@Param("id") Long id);
}
