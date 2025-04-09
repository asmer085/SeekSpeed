import requests
import time
from collections import Counter

# Configuration
EUREKA_SERVER = "http://localhost:8761"  # Eureka server URL
SERVICE_NAME = "users"                   # Registered service name in Eureka
ENDPOINT = "/users/all"                  # API endpoint to test
REQUEST_COUNT = 100                      # Number of requests to send

def get_service_instances():
    """Fetch all available instances from Eureka"""
    try:
        response = requests.get(
            f"{EUREKA_SERVER}/eureka/apps/{SERVICE_NAME}",
            headers={"Accept": "application/json"}
        )
        response.raise_for_status()

        data = response.json()
        if 'application' not in data or not data['application']['instance']:
            raise ValueError("No instances found")

        return data['application']['instance']
    except Exception as e:
        print(f"Error fetching service instances: {e}")
        return None

class RoundRobinBalancer:
    """Simple round-robin load balancer"""
    def __init__(self, instances):
        self.instances = instances
        self.current_index = 0

    def get_next_instance(self):
        instance = self.instances[self.current_index]
        self.current_index = (self.current_index + 1) % len(self.instances)
        return instance

# Discover available instances
instances = get_service_instances()
if not instances:
    print(f"Service '{SERVICE_NAME}' not found or no instances available")
    exit(1)

print(f"Discovered {len(instances)} instance(s):")
for i, instance in enumerate(instances):
    print(f"{i+1}. {instance['instanceId']} ({instance['hostName']}:{instance['port']['$']})")

# Initialize load balancer
balancer = RoundRobinBalancer(instances)

# Load testing
responses = []
start_time = time.time()

for i in range(REQUEST_COUNT):
    instance = balancer.get_next_instance()
    base_url = f"http://{instance['hostName']}:{instance['port']['$']}"
    service_url = f"{base_url}{ENDPOINT}"

    try:
        response = requests.get(service_url, timeout=5)
        if response.status_code == 200:
            instance_id = response.headers.get("X-Instance-Id", instance['instanceId'])
            responses.append(instance_id)
            print(f"Request {i+1}/{REQUEST_COUNT}: Success (Instance: {instance_id})")
        else:
            print(f"Request {i+1}/{REQUEST_COUNT}: Failed (Status: {response.status_code})")
    except Exception as e:
        print(f"Request {i+1}/{REQUEST_COUNT}: Error ({str(e)})")

# Calculate results
end_time = time.time()
counter = Counter(responses)
total_time = end_time - start_time
success_rate = (len(responses) / REQUEST_COUNT) * 100

# Print summary
print("\n=== Load Test Results ===")
print(f"Total Requests: {REQUEST_COUNT}")
print(f"Successful Requests: {len(responses)} ({success_rate:.2f}%)")
print(f"Time Elapsed: {total_time:.2f} seconds")
print(f"Requests per Second: {REQUEST_COUNT/total_time:.2f}")
print("\nRequests per Instance:")
for instance_id, count in counter.items():
    print(f"- {instance_id}: {count} requests ({count/REQUEST_COUNT*100:.1f}%)")