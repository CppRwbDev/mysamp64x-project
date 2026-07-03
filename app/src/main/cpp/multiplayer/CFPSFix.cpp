#include "CFPSFix.h"
#include "main.h"
#include <sys/syscall.h>

static void setThreadAffinityMask(pid_t tid, uint32_t mask)
{
	syscall(__NR_sched_setaffinity, tid, sizeof(mask), &mask);
}

void CFPSFix::Routine()
{
	while (true)
	{
		m_Mutex.lock();
		for (auto& i : m_Threads)
		{
            // Mask 0xF0 targets big cores (4,5,6,7) on 8-core CPUs
            // This ensures game threads run on high-performance cores
			uint32_t mask = 0xf0;

			setThreadAffinityMask(i, mask);
		}
		m_Mutex.unlock();

		std::this_thread::sleep_for(std::chrono::milliseconds(2000));
	}
}

CFPSFix::CFPSFix()
{
	std::thread(&CFPSFix::Routine, this).detach();
}

CFPSFix::~CFPSFix()
{
}

void CFPSFix::PushThread(pid_t tid)
{
	std::lock_guard<std::mutex> lock(m_Mutex);

	m_Threads.push_back(tid);
}
