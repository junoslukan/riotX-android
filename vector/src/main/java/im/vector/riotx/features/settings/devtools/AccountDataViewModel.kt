/*
 * Copyright (c) 2020 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.riotx.features.settings.devtools

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import im.vector.matrix.android.api.session.Session
import im.vector.matrix.android.internal.session.sync.model.accountdata.UserAccountData
import im.vector.matrix.rx.rx
import im.vector.riotx.core.platform.EmptyAction
import im.vector.riotx.core.platform.EmptyViewEvents
import im.vector.riotx.core.platform.VectorViewModel

data class AccountDataViewState(
        val accountData: Async<List<UserAccountData>> = Uninitialized
) : MvRxState

class AccountDataViewModel @AssistedInject constructor(@Assisted initialState: AccountDataViewState,
                                                       private val session: Session)
    : VectorViewModel<AccountDataViewState, EmptyAction, EmptyViewEvents>(initialState) {

    init {
        session.rx().liveAccountData(emptySet())
                .execute {
                    copy(accountData = it)
                }
    }

    override fun handle(action: EmptyAction) {}

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: AccountDataViewState): AccountDataViewModel
    }

    companion object : MvRxViewModelFactory<AccountDataViewModel, AccountDataViewState> {

        @JvmStatic
        override fun create(viewModelContext: ViewModelContext, state: AccountDataViewState): AccountDataViewModel? {
            val fragment: AccountDataFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
