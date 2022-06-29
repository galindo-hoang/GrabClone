import axios from 'axios';
class UserService {
    getUser() {
        return axios.get('/api/v1/users');
    }
}

export default new UserService();