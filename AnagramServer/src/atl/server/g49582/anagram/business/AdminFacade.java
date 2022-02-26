package atl.server.g49582.anagram.business;

import atl.server.g49582.anagram.dto.WordDto;
import atl.server.g49582.anagram.exception.BusinessException;
import atl.server.g49582.anagram.exception.FileException;
import atl.server.g49582.anagram.file.WordsFileReader;
import static atl.server.g49582.anagram.file.WordsFileReader.DEFAULT_READ_URL_FILE;
import java.util.List;

/**
 * Facade to the data level.
 *
 * @author jlc
 */
public final class AdminFacade {

    /**
     * Return the list of all words available.
     *
     * @return the list of all words available.
     * @throws BusinessException if the datas aren't readable.
     */
    public static List<WordDto> getAllWords() throws BusinessException {
        try {

            return WordsFileReader.readFile(DEFAULT_READ_URL_FILE);
        } catch (FileException eData) {
            String msg = eData.getMessage();
            try {
                msg = eData.getMessage() + "\n" + msg;
            } finally {
                throw new BusinessException("Liste des mots inaccessible! \n" + msg);
            }
        }

    }

    /**
     *
     * Constructs the <code> Anagram </code> application.
     *
     * Be aware of the private visibility.
     */
    private AdminFacade() {

    }
}