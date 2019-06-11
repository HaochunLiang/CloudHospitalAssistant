import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        isLogin: false,
        userType: -1,
        urls: [],
        diagnose: [],
        diagnoseType: true
    },
    mutations: {
        changeDiagnoseType (state, type) {
            state.diagnoseType = type
        },
        addDisease (state, disease) {
            state.diagnose.push(disease)
        },
        setDiagnose (state, diagnose) {
            state.diagnose = diagnose
        },
        clearDiagnose (state) {
            state.diagnose = []
            state.diagnoseType = 0
        },
        setLogin (state, login) {
            state.isLogin = login
        },
        setUserType (state, userType) {
            state.userType = userType
        },
        setUrls (state, urls) {
            state.urls = urls
        }
    },
    actions: {}
})