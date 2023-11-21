import request from '@/plugins/request';

export function getInvalidUrl () {
    return request({
        url: '/invalid-url',
        method: 'get'
    });
}
