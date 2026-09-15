import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 100,
  duration: '30s',
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8081';

export default function () {
  const res = http.get(`${BASE_URL}/students`);
  check(res, {
    'status is 200': (r) => r.status === 200,
  });
  sleep(0.1);
}
